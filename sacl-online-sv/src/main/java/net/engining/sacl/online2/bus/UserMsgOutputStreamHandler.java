package net.engining.sacl.online2.bus;

import net.engining.bustream.base.stream.AbstractOutputBustreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-10-31 18:20
 * @since :
 **/
//@Service
public class UserMsgOutputStreamHandler extends AbstractOutputBustreamHandler<User> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMsgOutputStreamHandler.class);

    @Override
    protected void transform(User event, Map<String, Object> headers) {
        LOGGER.info("transform event to user object: {}", event);

    }
}
