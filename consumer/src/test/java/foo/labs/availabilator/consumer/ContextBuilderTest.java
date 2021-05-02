package foo.labs.availabilator.consumer;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ContextBuilderTest {

    @Test
    public void shouldSetDefaultValueWhenNoVars() {
        Map<String, String> ctx = new ContextBuilder().build(new HashMap<>());

        assertThat(ctx.get(ContextBuilder.TOPIC), equalTo(ContextBuilder.DEFAULT_TOPIC));
    }

    @Test
    public void shouldSetCustomValueWhenCustomVars() {
        String customValue = "topic";
        Map<String, String> env = Map.of(ContextBuilder.TOPIC, customValue);

        Map<String, String> ctx = new ContextBuilder().build(env);

        assertThat(ctx.get(ContextBuilder.TOPIC), equalTo(customValue));
    }
}
