package io.github.tramchamploo.bufferslayer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class FakeSender implements Sender<TestMessage, Integer> {

  private AtomicBoolean closed = new AtomicBoolean(false);
  final List<TestMessage> sent = new CopyOnWriteArrayList<>();
  private Consumer<List<TestMessage>> onMessages = messages -> {};

  @Override
  public CheckResult check() {
    return CheckResult.OK;
  }

  @Override
  public void close() {
    closed.set(true);
  }

  public void onMessages(Consumer<List<TestMessage>> onMessages) {
    this.onMessages = onMessages;
  }

  @Override
  public List<Integer> send(List<TestMessage> messages) {
    onMessages.accept(messages);
    sent.addAll(messages);

    if (closed.get()) {
      throw new IllegalStateException("Closed!");
    }

    Integer[] ret = new Integer[messages.size()];
    for (int i = 0; i < messages.size(); i++) {
      ret[i] = messages.get(i).key;
    }
    return Arrays.asList(ret);
  }
}
