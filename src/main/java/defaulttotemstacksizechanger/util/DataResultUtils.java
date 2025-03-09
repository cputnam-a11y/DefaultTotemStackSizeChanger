package defaulttotemstacksizechanger.util;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.function.FailableSupplier;

public class DataResultUtils {
    public static DataResult<Unit> fromBool(boolean condition, String message) {
        return condition
               ? DataResult.success(Unit.INSTANCE)
               : DataResult.error(() -> message);
    }

    public static <R> DataResult<R> exceptionToResult(FailableSupplier<R, ? extends Throwable> getter) {
        try {
            return DataResult.success(getter.get());
        } catch (Throwable error) {
            return DataResult.error(error::getMessage);
        }
    }

    public static DataResult<Unit> exceptionToResult(FailableRunnable<? extends Throwable> task) {
        try {
            task.run();
            return DataResult.success(Unit.INSTANCE);
        } catch (Throwable error) {
            return DataResult.error(error::getMessage);
        }
    }
}
