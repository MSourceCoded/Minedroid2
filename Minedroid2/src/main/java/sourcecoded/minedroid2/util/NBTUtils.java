package sourcecoded.minedroid2.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.*;

public class NBTUtils {

	public static NBTBase getTag(String key, NBTTagCompound tag){
		String[] path = key.split("\\.");

		NBTTagCompound deepTag = tag;
		for (String i : path){
			if (deepTag.hasKey(i)){
				if (deepTag.getTag(i) instanceof NBTTagCompound)
					deepTag = deepTag.getCompoundTag(i);
				else{
					return deepTag.getTag(i);
				}
			} else {
				return null;
			}
		}
		return deepTag;
	}

	public static NBTTagCompound setTag(String key, NBTTagCompound targetTag, NBTBase addedTag){
		String[] path = key.split("\\.");

		NBTTagCompound deepTag = targetTag;
		for (int i = 0; i < path.length - 1; i++){
			if (!deepTag.hasKey(path[i]))
				//deepTag.setCompoundTag(path[i], new NBTTagCompound());
				deepTag.setTag(path[i], new NBTTagCompound());

			deepTag = deepTag.getCompoundTag(path[i]);
		}

		deepTag.setTag(path[path.length - 1], addedTag);		

		return targetTag;
	}

	public static NBTTagCompound createTag(NBTTagCompound inTag, HashSet<String> keys){
		if (keys.contains("*")) return inTag;

		NBTTagCompound outTag = new NBTTagCompound();

		for (String key : keys){
			NBTBase tagToAdd = getTag(key, inTag);
			//System.out.printf("%s\n", tagToAdd);
			if (tagToAdd != null)
				outTag = setTag(key, outTag, tagToAdd);
		}

		return outTag;
	}

	public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutputStream.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);

			if (abyte.length > 32000)
				par1DataOutputStream.writeShort(-1);
			else{
				par1DataOutputStream.writeShort((short)abyte.length);
				par1DataOutputStream.write(abyte);
			}
		}
	}	

	public static NBTTagCompound readNBTTagCompound(DataInputStream par0DataInputStream) throws IOException
	{
		short short1 = par0DataInputStream.readShort();

		if (short1 < 0)
		{
			return null;
		}
		else
		{
			byte[] abyte = new byte[short1];
			par0DataInputStream.readFully(abyte);
			//return CompressedStreamTools.decompress(abyte);
			return CompressedStreamTools.func_152457_a(abyte, NBTSizeTracker.field_152451_a);
		}
	}   	

	public static int getNBTInteger(NBTTagCompound tag, String keyname){
		NBTBase subtag = tag.getTag(keyname);
		if (subtag instanceof NBTTagInt)
			return tag.getInteger(keyname);
		if (subtag instanceof NBTTagShort)
			return tag.getShort(keyname);
		if (subtag instanceof NBTTagByte)
			return tag.getByte(keyname);

		return 0;
	}  

	/**
	 * Get the values of an NBTTagCompound as a hashmap containing Primitive Types
	 * @param tagCompound The compound to convert
	 * @return The HashMap 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap getCompoundAsMap(NBTTagCompound tagCompound) {
		HashMap tagMap = new HashMap();
		HashMap returnMap = new HashMap();
		Class nbtInstance = NBTTagCompound.class;
		Field nbtKeys;

		try {
			nbtKeys = nbtInstance.getDeclaredField("tagMap");
			nbtKeys.setAccessible(true);
			tagMap = (HashMap) nbtKeys.get(tagCompound);

			returnMap = loopOut(tagMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnMap;
	}

	/**
	 * Remove NBTTags from a hashmap and replace them with primitive types
	 * @param original The Hashmap to convert
	 * @return The Hashmap after conversion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })	//Ugh
	static HashMap loopOut(HashMap original) {
		HashMap output = new HashMap();

		Iterator iter = original.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry) iter.next();

			Class val = pairs.getValue().getClass();

			try {
				if (val.equals(NBTTagCompound.class)) {
					output.putAll(getCompoundAsMap((NBTTagCompound) pairs.getValue()));
				} else if (val.equals(NBTTagByteArray.class)) {
					
					Field data = val.getDeclaredField("byteArray");
					data.setAccessible(true);
					Object[] value = (Object[]) data.get(pairs.getValue());
					output.put(pairs.getKey(), value);
					
				} else if (val.equals(NBTTagIntArray.class)) {
					
					Field data = val.getDeclaredField("intArray");
					data.setAccessible(true);
					Object[] value = (Object[]) data.get(pairs.getValue());
					output.put(pairs.getKey(), value);
					
				} else if (val.equals(NBTTagList.class)) { 
					
					Field data = val.getDeclaredField("tagList");
					data.setAccessible(true);
					ArrayList value = (ArrayList) data.get(pairs.getValue());
					
					for (int i = 0; i < value.size(); i++) {
						if (value.get(i).getClass().equals(NBTTagCompound.class)) {
							NBTTagCompound compound = (NBTTagCompound) value.get(i);
							HashMap map = getCompoundAsMap(compound);
							value.set(i, map);
						}
					}
					
					output.put(pairs.getKey(), value);
					
				} else if (val.equals(NBTTagEnd.class)) {
					//Do not continue
					break;
				} else if (val.getSuperclass().equals(NBTBase.NBTPrimitive.class) || val.equals(NBTTagString.class)) {
					
					Field data = val.getDeclaredField("data");
					data.setAccessible(true);
					Object value = data.get(pairs.getValue());
					output.put(pairs.getKey(), value);
					
				}

				iter.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		return output;
	}

}
