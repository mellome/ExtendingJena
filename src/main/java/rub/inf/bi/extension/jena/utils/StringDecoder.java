package rub.inf.bi.extension.jena.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;

public class StringDecoder {
public static String decode(String encodedString) {
		
		String result = encodedString;
		
		if(encodedString.contains("\\X4\\")) {
			result = decodeX4(encodedString);
		}
		if(encodedString.contains("\\X2\\")) {
			result = decodeX2(encodedString);
		}
		
		return result;
	}
	
	private static String decodeX4(String encodedString) {
		String result = encodedString;
		
		while (result.contains("\\X4\\")) {
			int index = result.indexOf("\\X4\\");
			int indexOfEnd = result.indexOf("\\X0\\", index);
			if (indexOfEnd == -1) {
//				throw new DeserializeException(DeserializerErrorCode.STRING_ENCODING_X4_NOT_CLOSED_WITH_X0, lineNumber, "\\X4\\ not closed with \\X0\\");
			}
			if ((indexOfEnd - (index + 4)) % 8 != 0) {
//				throw new DeserializeException(DeserializerErrorCode.STRING_ENCODING_NUMBER_OF_HEX_CHARS_IN_X4_NOT_DIVISIBLE_BY_8, lineNumber, "Number of hex chars in \\X4\\ definition not divisible by 8");
			}
			try {
				
				byte[] byteArray = Hex.decodeHex(result.substring(index + 4, indexOfEnd).toCharArray());
				ByteBuffer buffer = ByteBuffer.wrap(byteArray);
				CharBuffer decode = Charset.forName("UTF-32").decode(buffer);
				result = result.substring(0, index) + decode.toString() + result.substring(indexOfEnd + 4);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		return result;
	}
	
	private static String decodeX2(String encodedString) {
		
		String result = encodedString;
		
		while (result.contains("\\X2\\")) {
			int index = result.indexOf("\\X2\\");
			int indexOfEnd = result.indexOf("\\X0\\", index);
			if (indexOfEnd == -1) {
//				throw new DeserializeException(DeserializerErrorCode.STRING_ENCODING_X2_NOT_CLOSED_WITH_X0, lineNumber, "\\X2\\ not closed with \\X0\\");
			}
			if ((indexOfEnd - index) % 4 != 0) {
//				throw new DeserializeException(DeserializerErrorCode.STRING_ENCODING_NUMBER_OF_HEX_CHARS_IN_X2_NOT_DIVISIBLE_BY_4, lineNumber, "Number of hex chars in \\X2\\ definition not divisible by 4");
			}
			try {
				ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(result.substring(index + 4, indexOfEnd).toCharArray()));
				CharBuffer decode = Charsets.UTF_16BE.decode(buffer);
				result = result.substring(0, index) + decode.toString() + result.substring(indexOfEnd + 4);
			} catch (Exception e) {
//				throw new DeserializeException(DeserializerErrorCode.STRING_ENCODING_CHARACTER_DECODING_EXCEPTION, lineNumber, e);
				e.printStackTrace();
			}
		}
		return result;
	}


}
