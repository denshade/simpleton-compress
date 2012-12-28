import static org.junit.Assert.*;

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
	}

	
}
