/**
 * 
 */
package ask.atg.chat.server.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Anders Skoglund
 *
 */
public final class JsonParser
{
    // Initialize logger
    private static Logger logger = LoggerFactory.getLogger(JsonParser.class);

    /**
     * Parse a JSON {@link String} into a {@link JsonBean}.
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static <T extends JsonBean> Optional<T> fromJson(String json, Class<T> clazz)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            return Optional.of(mapper.readValue(json, clazz));
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Write a {@link JsonBean} as a JSON {@link String}.
     * 
     * @param bean
     * @return
     */
    public static <T extends JsonBean> Optional<String> toJson(T bean)
    {
        return toJsonString(bean);
    }

    /**
     * Write a collection of {@link JsonBean} as a JSON {@link String}.
     * 
     * @param bean
     * @return
     */
    public static <T extends JsonBean> Optional<String> toJson(Collection<T> beans)
    {
        return toJsonString(beans);
    }

    /**
     * Logic for parsing {@link JsonBean} into JSON {@link String}.
     * 
     * @param bean
     * @return
     */
    private static <T extends JsonBean> Optional<String> toJsonString(Object object)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            return Optional.of(mapper.writeValueAsString(object));
        }
        catch (JsonProcessingException e)
        {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    private JsonParser()
    {
    }
}
