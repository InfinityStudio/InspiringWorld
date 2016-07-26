package api.simplelib.coremod;

import com.google.common.collect.ImmutableList;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * @author ci010
 */
public abstract class Transformer implements IClassTransformer
{
	private ImmutableList<ClassPatch> lst;
	private boolean isObfscated;

	{
		ImmutableList.Builder<ClassPatch> builder = ImmutableList.builder();
		this.buildClassPatch(builder);
		lst = builder.build();
	}

	protected abstract void buildClassPatch(ImmutableList.Builder<ClassPatch> builder);

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		isObfscated = !name.equals(transformedName);
		for (ClassPatch patch : lst)
			if (patch.patchClass().equals(transformedName))
				return handle(patch, bytes);
		return bytes;
	}

	private byte[] handle(ClassPatch patch, byte[] original)
	{
		try
		{
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			new ClassReader(original).accept(patch.accept(writer), 0);
			return writer.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return original;
	}
}
