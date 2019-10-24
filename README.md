# android-matrix-algorithms
Java library to be used on Android, simply applies precomputed matrices etc.

## Algorithms
* [Simple PLS (SIMPLS)](http://www.statsoft.com/textbook/partial-least-squares/#SIMPLS)
* [Savitzky-Golay](https://en.wikipedia.org/wiki/Savitzky%E2%80%93Golay_filter)
* Standardize

## Usage
Use the following code to load the preprocessing map from the serialized file
`map.bin` (with `one` and `two` named pipelines) and apply it to data (processing
the same data with both pre-processing pipelines):

```java
import com.github.waikatodatamining.androidmatrix.PreprocessingMap;
import java.io.FileInputStream;
import java.utils.Map;
import java.utils.HashMap;
 
PreprocessingMap preprocessingMap;
preprocessingMap = new PreprocessingMap(new FileInputStream("map.bin"));

double[] data = ...;
Map<String, double[]> map = new HashMap<>();
map.put("one", data);
map.put("two", data);

// for ordered application
double[][] processedOrdered = preprocessingMap.applyOrdered(data);
 
// for mapped application
Map<String, double[]> processedMapped = preprocessingMap.apply(data);
```

## Android
See the following StackOverflow post for how to wrap a `java.nio.ByteBuffer` in
a `java.io.InputStream`:

https://stackoverflow.com/a/6603018/4698227

