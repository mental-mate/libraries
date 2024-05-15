package tech.harmonysoft.oss.mentalmate.util.checksum

import java.io.InputStream
import java.security.MessageDigest

object ChecksumUtil {

    @OptIn(ExperimentalStdlibApi::class)
    fun calculateChecksum(source: InputStream, algorithm: String = "SHA-256"): String {
        val digest = MessageDigest.getInstance(algorithm)
        val buffer = ByteArray(1024)
        var read = source.read(buffer, 0, buffer.size)
        while (read >= 0) {
            digest.update(buffer, 0, read)
            read = source.read(buffer, 0, buffer.size)
        }
        return digest.digest().toHexString()
    }
}