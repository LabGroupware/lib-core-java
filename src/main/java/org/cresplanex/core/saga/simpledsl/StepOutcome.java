package org.cresplanex.core.saga.simpledsl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.cresplanex.core.saga.orchestration.command.CommandWithDestinationAndType;

/**
 * 各サガステップの結果を表す抽象クラス。
 * サガステップの結果をコンシューマーで処理できるようにします。
 */
public abstract class StepOutcome {

  /**
   * ステップの結果に応じて、適切なコンシューマーを呼び出します。
   *
   * @param localConsumer ローカル処理の場合のコンシューマー
   * @param commandsConsumer リモートコマンド処理の場合のコンシューマー
   */
  public abstract void visit(Consumer<Optional<RuntimeException>> localConsumer, Consumer<List<CommandWithDestinationAndType>> commandsConsumer);

  /**
   * ローカルステップの結果を表すクラス。
   */
  static class LocalStepOutcome extends StepOutcome {
    private final Optional<RuntimeException> localOutcome;

    public LocalStepOutcome(Optional<RuntimeException> localOutcome) {
      this.localOutcome = localOutcome;
    }

    @Override
    public void visit(Consumer<Optional<RuntimeException>> localConsumer, Consumer<List<CommandWithDestinationAndType>> commandsConsumer) {
        localConsumer.accept(localOutcome);
    }
  }

  /**
   * リモートステップの結果を表すクラス。
   */
  static class RemoteStepOutcome extends StepOutcome {
    private final List<CommandWithDestinationAndType> commandsToSend;

    public RemoteStepOutcome(List<CommandWithDestinationAndType> commandsToSend) {
      this.commandsToSend = commandsToSend;
    }

    @Override
    public void visit(Consumer<Optional<RuntimeException>> localConsumer, Consumer<List<CommandWithDestinationAndType>> commandsConsumer) {
      commandsConsumer.accept(commandsToSend);
    }
  }

  /**
   * ローカル処理の結果を生成します。
   *
   * @param localOutcome ローカル処理で発生した例外（例外がなければ空）
   * @return ローカルステップ結果のインスタンス
   */
  public static StepOutcome makeLocalOutcome(Optional<RuntimeException> localOutcome) {
    return new LocalStepOutcome(localOutcome);
  }

  /**
   * リモートコマンド処理の結果を生成します。
   *
   * @param commandsToSend 実行するリモートコマンドのリスト
   * @return リモートステップ結果のインスタンス
   */
  public static StepOutcome makeRemoteStepOutcome(List<CommandWithDestinationAndType> commandsToSend) {
    return new RemoteStepOutcome(commandsToSend);
  }
}