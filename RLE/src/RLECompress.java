import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RLECompress 
{
	public static void main(String[] args) throws IOException
	{
		if (args.length < 2)
		{
			System.out.println("Usage: RLECompress <c|d> <inputFile>");			
		}
		boolean compress= true;
		if (args[0].equals("d"))
		{
			compress = false;
		}
		File sourceFile = new File(args[1]);
		File destinationFile = new File(args[1]+".rle");
		
		RLECompress c = new RLECompress();
		if (compress)
			c.compress(sourceFile, destinationFile);
		else
			c.decompress(sourceFile, destinationFile);
	}
	
	public static final byte  conversionByte = (byte)254;
	
	public void compress(File sourceFile, File destinationFile) throws IOException
	{
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
		
		byte[] readBytes = new byte[65536];
		int bytesRead = inputStream.read(readBytes) ;
		while (bytesRead > 0)
		{
			byte[] src = Arrays.copyOf(readBytes, bytesRead);
			outputStream.write(compressBuffer(src));
			bytesRead = inputStream.read(readBytes) ;
		}
		inputStream.close();
		outputStream.close();
	}
	
	public void decompress(File sourceFile, File destinationFile) throws IOException
	{
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
		
		byte[] readBytes = new byte[65536];
		int bytesRead = inputStream.read(readBytes) ;
		
		while (bytesRead  > 0)
		{
			byte[] src = Arrays.copyOf(readBytes, bytesRead);
			outputStream.write(decompressBuffer(src));
			bytesRead = inputStream.read(readBytes) ;
		}
		inputStream.close();
		outputStream.close();
	}
	
	protected byte[] decompressBuffer(byte[] buffer)
	{
		List<Byte> converted = new ArrayList<Byte>();
		for (int current = 0; current < buffer.length; current++)
		{
			if (buffer[current] == conversionByte)
			{
				int size = (int)buffer[current + 1] & 0xff;
				if (size == 0)
				{
					converted.add(conversionByte);current++;
				}
				else
				{
					byte theRepeatByte = buffer[current+2];
					for (int loop = 0; loop < size; loop++)
					{
						converted.add(theRepeatByte);
					}
					current += 2;
				}
			}else
			{
				converted.add(buffer[current]);
			}
		}
		return convertToPrimitiveArray(converted);
	}
	
	protected byte[] compressBuffer(byte[] buffer)
	{
		List<Byte> converted = new ArrayList<Byte>();
		List<Byte> decisionBuffer = new ArrayList<Byte>();
		for (int current = 0; current < buffer.length; current++)
		{
			byte currentByte = buffer[current];
			if (hasPreviousElement(decisionBuffer))
			{
				if (decisionBuffer.get(0) != currentByte || decisionBuffer.size() == 255)
				{
					writeDecisionBuffer(decisionBuffer, converted);
					decisionBuffer.clear();
				}
			}
			decisionBuffer.add(currentByte);
		}
		if (decisionBuffer.size() > 0)
		{
			writeDecisionBuffer(decisionBuffer, converted);
		}
		return convertToPrimitiveArray(converted);
	}
	
	private byte[] convertToPrimitiveArray(List<Byte> converted) {
		byte[] answer = new byte [converted.size()];
		
		int a = 0;
		for (byte b: converted)
		{
			answer[a++] = b;
		}
		return answer;
	}
	
	private void writeDecisionBuffer(final List<Byte> decisionBuffer,
			List<Byte> converted) {
		if (decisionBuffer.size() < 3)
		{
			for(byte val : decisionBuffer)
			{
				if (val == conversionByte)
				{
					converted.add(conversionByte);
					converted.add((byte)0);
				}else
				{
					converted.add(val);
	
				}
			}
		}
		else
		{
			converted.add(conversionByte);
			converted.add((byte)decisionBuffer.size());
			converted.add(decisionBuffer.get(0));
		}
	}
	private boolean hasPreviousElement(List<Byte> decisionBuffer) {
		return decisionBuffer.size() > 0;
	}
	
	
	
}
