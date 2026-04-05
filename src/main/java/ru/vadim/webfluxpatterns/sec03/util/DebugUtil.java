package ru.vadim.webfluxpatterns.sec03.util;

import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import tools.jackson.databind.ObjectMapper;

public class DebugUtil {

    public static void print(OrchestrationRequestContext context) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(context));
    }
}
