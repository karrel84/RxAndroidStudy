package karrel.com.calculatorreducesample;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by Rell on 2018. 5. 14..
 */
public class JavaUnitTest {

    @Test
    public void streamTest() {
        Integer[] arr = {100, 200, 300};
        Observable<Integer> source = Observable.fromArray(arr);
        source.subscribe(System.out::println);
    }
}
