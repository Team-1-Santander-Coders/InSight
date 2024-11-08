package getterson.insight.utils.exception;

@FunctionalInterface
public interface FunctionWrapper<T, R> {
    R apply(T t) throws Exception;
}
