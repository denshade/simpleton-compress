import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;


public class RLECompressTest {

	@Test
	public void testCompress() {
		RLECompress c = new RLECompress();
		assertArrayEquals(new byte[]{1,2,3}, c.compressBuffer(new byte[]
				{
					1,2,3
				}));
		
		assertArrayEquals(new byte[]{(byte)254,3,2}, c.compressBuffer(new byte[]
				{
					2,2,2
				}));
		
		assertArrayEquals(new byte[]{(byte)254,5,2}, c.compressBuffer(new byte[]
				{
					2,2,2,2,2
				}));
		assertArrayEquals(new byte[]{(byte)254,0}, c.compressBuffer(new byte[]
				{
					(byte)254
				}));
		byte[] lotsofTwos = new byte[255];
		for (int i = 0 ; i < lotsofTwos.length;i++)
			lotsofTwos[i] = 2;
		assertArrayEquals(new byte[]{(byte)254,(byte)255,2}, c.compressBuffer(lotsofTwos));
		byte[] lotsofOverTheTopTwos = new byte[256];
		for (int i = 0 ; i < lotsofOverTheTopTwos.length;i++)
			lotsofOverTheTopTwos[i] = 2;
		assertArrayEquals(new byte[]{(byte)254,(byte)255,2, 2}, c.compressBuffer(lotsofOverTheTopTwos));

	}
	
	@Test
	public void testDeCompress() {
		RLECompress c = new RLECompress();
		assertArrayEquals(new byte[]{1,2,3}, c.decompressBuffer(new byte[]
				{
					1,2,3
				}));
		
		assertArrayEquals(new byte[]
				{
				2,2,2
			}, c.decompressBuffer(new byte[]{(byte)254,3,2}));
		
		assertArrayEquals(new byte[]
				{
				2,2,2,2,2
			}, c.decompressBuffer(new byte[]{(byte)254,5,2}));
		assertArrayEquals(new byte[]
				{
				(byte)254
			}, c.decompressBuffer(new byte[]{(byte)254,0}));

		byte[] lotsofOverTheTopTwos = new byte[256];
		for (int i = 0 ; i < lotsofOverTheTopTwos.length;i++)
			lotsofOverTheTopTwos[i] = 2;
		assertArrayEquals(lotsofOverTheTopTwos, c.decompressBuffer(new byte[]{(byte)254,(byte)255,2, 2}));

	}
	
	@Test
	public void compressDecompress() throws IOException
	{
		FileWriter writer = new FileWriter(new File("hello.txt"));
		writer.write("heeeeeeeeelloo.txt");
		writer.close();
		RLECompress c = new RLECompress();
		c.compress(new File("hello.txt"), new File("hello.txt.rle"));
		c.decompress(new File("hello.txt.rle"), new File("hello2.txt"));
		BufferedReader reader = new BufferedReader (new FileReader(new File("hello2.txt")));
		String s = reader.readLine();
		assertEquals("heeeeeeeeelloo.txt", s);
	}

	
}
