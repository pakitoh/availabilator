package foo.labs.availabilator.checker;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ContextBuilderTest {

    @Test
    public void shouldSetDefaultValueWhenNoVars() {
        Map<String, String> ctx = new ContextBuilder().build(new HashMap<>());

        assertThat(ctx.get(ContextBuilder.POOLING_RATE), equalTo(ContextBuilder.DEFAULT_POOLING_RATE));
    }

    @Test
    public void shouldSetCustomValueWhenCustomVars() {
        String customValue = "1000";
        Map<String, String> env = Map.of(ContextBuilder.POOLING_RATE, customValue);

        Map<String, String> ctx = new ContextBuilder().build(env);

        assertThat(ctx.get(ContextBuilder.POOLING_RATE), equalTo(customValue));
    }
}
