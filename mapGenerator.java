package hw1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ThreadLocalRandom;



public class mapGenerator
{
	public static void main(String[] args) throws Exception
	{
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
		int mapR = 100;
		int mapC = 100;
		writer.write("BFS\n");
		writer.write(mapR-- + " " + mapC-- + "\n");
		writer.write("0 0\n");
		
		int min = 10, max = 20;
		
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int ePower = ThreadLocalRandom.current().nextInt(min, max + 1);
		int eMid = ThreadLocalRandom.current().nextInt(-100, 100);
		writer.write(ePower+"\n");
		writer.write(1+"\n");
		writer.write(mapR++ + " " + mapC++ + "\n");
		
		for (int i = 0; i < mapR; i++)
		{
			for (int j = 0; j < mapC; j++)
			{
				int n = ThreadLocalRandom.current().nextInt(eMid-2*ePower, eMid+2*ePower);
				writer.write(n+" ");
			}
			writer.write("\n");
		}
		writer.close();
	}
}
