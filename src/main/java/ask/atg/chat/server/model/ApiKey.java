/**
 * 
 */
package ask.atg.chat.server.model;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 
 * 
 * @author Anders Skoglund
 *
 */
public interface ApiKey
{
    String getSecret();

    public static ApiKey create()
    {
        return new ApiKey()
        {
            private String encodedKey = generate();

            @Override
            public String getSecret()
            {
                return encodedKey;
            }
        };
    }

    public static String generate()
    {
        // create new key
        SecretKey secretKey = null;
        try
        {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        // get base64 encoded version of the key to work with JWT
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
