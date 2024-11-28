import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pytorchmodel.R
import org.pytorch.IValue
import org.pytorch.Module
import org.tensorflow.lite.Tensor
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    // Load the model in onCreate or elsewhere
    private lateinit var pytorchModule: Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load your PyTorch model
        pytorchModule = Module.load(assetFilePath("diabetes.ptl"))

        // Define your input symptoms as a 1x8 float array (Example values)
        val inputSymptoms = floatArrayOf(5.2f, 7.5f, 1.5f, 3.2f, 6.3f, 4.5f, 2.1f, 5.0f)

        // Convert the float array to a tensor
        val inputTensor = Tensor.fromBlob(inputSymptoms, longArrayOf(1, 8))

        // Run the model with the input
        val output = pytorchModule.forward(IValue.from(inputTensor)).toTensor()

        // Get the output as a Float32 array (single value prediction, e.g., 0 for no diabetes, 1 for diabetes)
        val outputArray = output.dataAsFloatArray

        // Log the result
        Log.d("DiabetesPrediction", "Output: ${outputArray[0]}")
    }

    // Function to load model from assets folder
    private fun assetFilePath(assetName: String): String {
        val file = File(cacheDir, assetName)
        if (!file.exists()) {
            assets.open(assetName).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return file.absolutePath
    }
}
