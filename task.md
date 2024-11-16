# Task

## Docker Image

login
``` sh
docker login
```

build
``` sh
docker build -t ablankz/postgres:1.1.0 postgres
docker build -f debezium/postgres/Dockerfile  -t ablankz/debezium:1.0.0 .
```

push
``` sh
docker push ablankz/postgres:1.0.0
docker push ablankz/debezium:1.0.0
```

## Maven Central Publish
```sh
vim ~/.gradle/gradle.properties
```

```
ossrhUsername=yourSonatypeUsername
ossrhPassword=yourSonatypePassword
signing.keyId=yourPGPKeyId
signing.secretKeyRingFile=/path/to/your/secret.key
signing.password=yourPGPKeyPassword
```

### PGP作成

``` sh
omniarchy@hayashi:~/.m2/repository/org/cresplanex/core/1.0.0$ gpg --full-generate-key
gpg (GnuPG) 2.4.4; Copyright (C) 2024 g10 Code GmbH
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

gpg: directory '/home/omniarchy/.gnupg' created
gpg: keybox '/home/omniarchy/.gnupg/pubring.kbx' created
Please select what kind of key you want:
   (1) RSA and RSA
   (2) DSA and Elgamal
   (3) DSA (sign only)
   (4) RSA (sign only)
   (9) ECC (sign and encrypt) *default*
  (10) ECC (sign only)
  (14) Existing key from card
Your selection? 1
RSA keys may be between 1024 and 4096 bits long.
What keysize do you want? (3072) 4096
Requested keysize is 4096 bits
Please specify how long the key should be valid.
         0 = key does not expire
      <n>  = key expires in n days
      <n>w = key expires in n weeks
      <n>m = key expires in n months
      <n>y = key expires in n years
Key is valid for? (0)
Key does not expire at all
Is this correct? (y/N) y

GnuPG needs to construct a user ID to identify your key.

Real name: Hayashi Kenta
Email address: k.hayashi@cresplanex.com
Comment: Personal Key
You selected this USER-ID:
    "Hayashi Kenta (Personal Key) <k.hayashi@cresplanex.com>"

Change (N)ame, (C)omment, (E)mail or (O)kay/(Q)uit? O
We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
gpg: /home/omniarchy/.gnupg/trustdb.gpg: trustdb created
gpg: directory '/home/omniarchy/.gnupg/openpgp-revocs.d' created
gpg: revocation certificate stored as '/home/omniarchy/.gnupg/openpgp-revocs.d/69365BE88AD6D5BF46DC62C217B917047ACB4096.rev'
public and secret key created and signed.

pub   rsa4096 2024-11-08 [SC]
      69365BE88AD6D5BF46DC62C217B917047ACB4096
uid                      Hayashi Kenta (Personal Key) <k.hayashi@cresplanex.com>
sub   rsa4096 2024-11-08 [E]
```

keyID取得
```sh
gpg --list-keys
```

秘密鍵エクスポート
```sh
gpg --armor --export-secret-keys <YourKeyID> > keys/secret.key
```

Publish
```sh
 ./gradlew clean sonatypeCentralUpload
```

