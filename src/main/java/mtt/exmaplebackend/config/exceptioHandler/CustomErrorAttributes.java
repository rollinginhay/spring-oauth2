package mtt.exmaplebackend.config.exceptioHandler;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    /*
     * Providing a custom ErrorAttributes with an overridden getAttributes that returns a Map will override the default behavior (/error page) and return json instead
     * */

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attrs = super.getErrorAttributes(webRequest, options);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", attrs.get("timestamp"));
        body.put("status", attrs.get("status"));
        body.put("error", attrs.get("error"));
        body.put("message", attrs.get("message"));
        body.put("path", attrs.get("path"));

        return body;
    }
}
