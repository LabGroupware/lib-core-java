package org.cresplanex.core.messaging.kafka.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.cresplanex.core.messaging.kafka.common.sbe.MessageHeaderDecoder;
import org.cresplanex.core.messaging.kafka.common.sbe.MessageHeaderEncoder;
import org.cresplanex.core.messaging.kafka.common.sbe.MultiMessageDecoder;
import org.cresplanex.core.messaging.kafka.common.sbe.MultiMessageEncoder;

public class CoreKafkaMultiMessageConverter {

    public static int HEADER_SIZE = MessageHeaderEncoder.ENCODED_LENGTH + MultiMessageEncoder.MessagesEncoder.HEADER_SIZE + MultiMessageEncoder.HeadersEncoder.HEADER_SIZE;
    public static final String MAGIC_ID = "a8c79db675e14c4cbf1eb77d0d6d0f00"; // generated UUID
    public static final byte[] MAGIC_ID_BYTES = CoreBinaryMessageEncoding.stringToBytes(MAGIC_ID);

    public byte[] convertMessagesToBytes(List<CoreKafkaMultiMessage> coreKafkaMultiMessages) {
        return convertMessagesToBytes(new CoreKafkaMultiMessages(Collections.emptyList(), coreKafkaMultiMessages));
    }

    public byte[] convertMessagesToBytes(CoreKafkaMultiMessages coreKafkaMultiMessages) {

        MessageBuilder builder = new MessageBuilder();

        builder.setHeaders(coreKafkaMultiMessages.getHeaders());

        for (CoreKafkaMultiMessage message : coreKafkaMultiMessages.getMessages()) {
            builder.addMessage(message);
        }

        return builder.toBinaryArray();
    }

    public CoreKafkaMultiMessages convertBytesToMessages(byte[] bytes) {

        if (!isMultiMessage(bytes)) {
            throw new RuntimeException("WRONG MAGIC NUMBER!");
        }

        MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

        MutableDirectBuffer buffer = new UnsafeBuffer(bytes);

        messageHeaderDecoder.wrap(buffer, 0);

        // final int templateId = messageHeaderDecoder.templateId();
        final int actingBlockLength = messageHeaderDecoder.blockLength();
        final int actingVersion = messageHeaderDecoder.version();

        MultiMessageDecoder multiMessageDecoder = new MultiMessageDecoder().wrap(buffer, messageHeaderDecoder.encodedLength(), actingBlockLength, actingVersion);

        List<CoreKafkaMultiMessagesHeader> headers = decodeCoreKafkaMultiMessagesHeaders(multiMessageDecoder);
        List<CoreKafkaMultiMessage> messages = decodeCoreKafkaMultiMessages(multiMessageDecoder);

        return new CoreKafkaMultiMessages(headers, messages);
    }

    private List<CoreKafkaMultiMessagesHeader> decodeCoreKafkaMultiMessagesHeaders(MultiMessageDecoder multiMessageDecoder) {
        MultiMessageDecoder.HeadersDecoder headersDecoder = multiMessageDecoder.headers();
        List<CoreKafkaMultiMessagesHeader> headers = new ArrayList<>();

        for (int i = 0; i < headersDecoder.count(); i++) {
            headersDecoder.next();
            int keyLength = headersDecoder.keyLength();
            byte[] keyBytes = new byte[keyLength];
            headersDecoder.getKey(keyBytes, 0, keyLength);
            int valueLength = headersDecoder.valueLength();
            byte[] valueBytes = new byte[valueLength];
            headersDecoder.getKey(valueBytes, 0, valueLength);

            String key = CoreBinaryMessageEncoding.bytesToString(keyBytes);
            String value = CoreBinaryMessageEncoding.bytesToString(valueBytes);

            headers.add(new CoreKafkaMultiMessagesHeader(key, value));
        }

        return headers;
    }

    private List<CoreKafkaMultiMessage> decodeCoreKafkaMultiMessages(MultiMessageDecoder multiMessageDecoder) {
        MultiMessageDecoder.MessagesDecoder messagesDecoder = multiMessageDecoder.messages();
        List<CoreKafkaMultiMessage> messages = new ArrayList<>();

        for (int i = 0; i < messagesDecoder.count(); i++) {
            messagesDecoder.next();

            List<CoreKafkaMultiMessageHeader> messageHeaders = decodeCoreKafkaMultiMessageHeaders(messagesDecoder);

            int keyLength = messagesDecoder.keyLength();
            byte[] keyBytes = new byte[keyLength];
            messagesDecoder.getKey(keyBytes, 0, keyLength);
            int valueLength = messagesDecoder.valueLength();
            byte[] valueBytes = new byte[valueLength];
            messagesDecoder.getKey(valueBytes, 0, valueLength);

            String key = CoreBinaryMessageEncoding.bytesToString(keyBytes);
            String value = CoreBinaryMessageEncoding.bytesToString(valueBytes);

            messages.add(new CoreKafkaMultiMessage(key, value, messageHeaders));
        }

        return messages;
    }

    private List<CoreKafkaMultiMessageHeader> decodeCoreKafkaMultiMessageHeaders(MultiMessageDecoder.MessagesDecoder messagesDecoder) {
        List<CoreKafkaMultiMessageHeader> messageHeaders = new ArrayList<>();
        MultiMessageDecoder.MessagesDecoder.HeadersDecoder messageHeadersDecoder = messagesDecoder.headers();

        for (int j = 0; j < messageHeadersDecoder.count(); j++) {
            messageHeadersDecoder.next();

            int keyLength = messageHeadersDecoder.keyLength();
            byte[] keyBytes = new byte[keyLength];
            messageHeadersDecoder.getKey(keyBytes, 0, keyLength);
            int valueLength = messageHeadersDecoder.valueLength();
            byte[] valueBytes = new byte[valueLength];
            messageHeadersDecoder.getKey(valueBytes, 0, valueLength);

            String key = CoreBinaryMessageEncoding.bytesToString(keyBytes);
            String value = CoreBinaryMessageEncoding.bytesToString(valueBytes);

            messageHeaders.add(new CoreKafkaMultiMessageHeader(key, value));
        }

        return messageHeaders;
    }

    public List<String> convertBytesToValues(byte[] bytes) {
        if (isMultiMessage(bytes)) {
            return convertBytesToMessages(bytes)
                    .getMessages()
                    .stream()
                    .map(CoreKafkaMultiMessage::getValue)
                    .collect(Collectors.toList());
        } else {
            return Collections.singletonList(CoreBinaryMessageEncoding.bytesToString(bytes));
        }
    }

    public boolean isMultiMessage(byte[] message) {
        if (message.length < MAGIC_ID_BYTES.length) {
            return false;
        }

        for (int i = 0; i < MAGIC_ID_BYTES.length; i++) {
            if (message[i] != MAGIC_ID_BYTES[i]) {
                return false;
            }
        }

        return true;
    }

    public static class MessageBuilder {

        private Optional<Integer> maxSize;
        private int size;
        private List<CoreKafkaMultiMessagesHeader> headers = Collections.emptyList();
        private final List<CoreKafkaMultiMessage> messagesToWrite = new ArrayList<>();

        public MessageBuilder(int maxSize) {
            this(Optional.of(maxSize));
        }

        public MessageBuilder() {
            this(Optional.empty());
        }

        public MessageBuilder(Optional<Integer> maxSize) {
            this.maxSize = maxSize;

            size = HEADER_SIZE;
        }

        public int getSize() {
            return size;
        }

        public boolean setHeaders(List<CoreKafkaMultiMessagesHeader> headers) {
            int estimatedSize = KeyValue.estimateSize(headers);

            if (isSizeOverLimit(estimatedSize)) {
                return false;
            }

            this.headers = headers;

            size += estimatedSize;

            return true;
        }

        public boolean addMessage(CoreKafkaMultiMessage message) {
            int estimatedSize = message.estimateSize();

            if (isSizeOverLimit(estimatedSize)) {
                return false;
            }

            messagesToWrite.add(message);

            size += estimatedSize;

            return true;
        }

        private boolean isSizeOverLimit(int estimatedSize) {
            return maxSize.map(ms -> size + estimatedSize > ms).orElse(false);
        }

        public byte[] toBinaryArray() {

            ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(2 * size); // Think about the size

            MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();

            MessageHeaderEncoder he = messageHeaderEncoder.wrap(buffer, 0);

            for (int i = 0; i < CoreKafkaMultiMessageConverter.MAGIC_ID_BYTES.length; i++) {
                byte b = CoreKafkaMultiMessageConverter.MAGIC_ID_BYTES[i];
                he.magicBytes(i, b);
            }

            MultiMessageEncoder multiMessageEncoder = new MultiMessageEncoder().wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);

            MultiMessageEncoder.HeadersEncoder headersEncoder = multiMessageEncoder.headersCount(headers.size());
            headers.forEach(header -> headersEncoder.next().key(header.getKey()).value(header.getValue()));

            MultiMessageEncoder.MessagesEncoder messagesEncoder = multiMessageEncoder.messagesCount(messagesToWrite.size());
            messagesToWrite.forEach(message -> {
                messagesEncoder.next();

                MultiMessageEncoder.MessagesEncoder.HeadersEncoder messageHeadersEncoder
                        = messagesEncoder.headersCount(message.getHeaders().size());

                for (int i = 0; i < message.getHeaders().size(); i++) {
                    CoreKafkaMultiMessageHeader header = message.getHeaders().get(i);
                    messageHeadersEncoder.next().key(header.getKey()).value(header.getValue());
                }

                messagesEncoder.key(message.getKey()).value(message.getValue());
            });

            return Arrays.copyOfRange(buffer.byteArray(), 0, multiMessageEncoder.encodedLength() + messageHeaderEncoder.encodedLength());
        }
    }
}
