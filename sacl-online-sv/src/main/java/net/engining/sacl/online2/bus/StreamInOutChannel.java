package net.engining.sacl.online2.bus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义的 Stream Channel，需要通过@EnableBinding启用
 *
 * @author Eric Lu
 * @date 2020-09-23 19:34
 **/
public interface StreamInOutChannel {

    String INPUT = "msgInput";

    String OUTPUT = "msgOutput";

    String POLLINPUT = "msgPollableInput";

    @Output(StreamInOutChannel.OUTPUT)
    MessageChannel msgOutput();

    @Input(StreamInOutChannel.INPUT)
    SubscribableChannel msgInput();

    @Input(StreamInOutChannel.POLLINPUT)
    PollableMessageSource msgPollableInput();

}
