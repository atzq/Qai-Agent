package net.itzq.mira.modules.ai.client.openai.chat.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.itzq.mira.modules.ai.client.openai.chat.entity.Content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ContentDeserializer
 */
public class ContentDeserializer extends JsonDeserializer<Content> {
    @Override
    public Content deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isTextual()) {
            return Content.ofText(node.asText());
        } else if (node.isArray()) {
            List<Content.MultiModal> parts = new ArrayList<>();
            for (JsonNode element : node) {
                Content.MultiModal part = p.getCodec().treeToValue(element, Content.MultiModal.class);
                parts.add(part);
            }
            return Content.ofMultiModals(parts);
        }
        throw new IOException("Unsupported content format");
    }
}
