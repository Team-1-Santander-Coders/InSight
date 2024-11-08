package getterson.insight.utils.exception;

import getterson.insight.exceptions.TopicNotFoundException;
import getterson.insight.exceptions.WrappedTopicNotFoundException;

import java.util.function.Function;

public class ThrowingFunctionWrapper {
    public static <T, R> Function<T, R> wrap(FunctionWrapper<T, R> throwingFunction) {
        return item -> {
            try {
                return throwingFunction.apply(item);
            } catch (RuntimeException e) {
                throw e;
            } catch (TopicNotFoundException e) {
                throw new WrappedTopicNotFoundException("Erro ao aplicar função com TopicNotFoundException", e);
            } catch (Exception e) {
                throw new RuntimeException("Erro desconhecido ao aplicar função", e);
            }
        };
    }
}
