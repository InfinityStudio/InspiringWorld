package net.infstudio.inspiringworld.magic.repackage.api.simplelib.coremod;

import com.google.common.collect.Lists;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * @author ci010
 */
public abstract class ClassPatch extends ClassVisitor
{
	private List<MethodPatch> patches;

	public ClassPatch()
	{
		super(Opcodes.ASM4);
	}

	public ClassPatch accept(ClassVisitor visitor)
	{
		this.cv = visitor;
		return this;
	}

	public abstract String patchClass();

	public ClassPatch patchMethod(MethodPatch patch)
	{
		if (patches == null)
			patches = Lists.newLinkedList();
		this.patches.add(patch);
		return this;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		for (MethodPatch patch : patches)
			if (patch.match(name, desc))
				return patch.apply(super.visitMethod(access, name, desc, signature, exceptions));
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public static class MethodPatch extends MethodVisitor
	{
		private String name, descriptor;

		public boolean match(String name, String desc)
		{
			System.out.println("Try to match " + name + " " + desc);
			return this.name.equals(name) && this.descriptor.equals(desc);
		}

		public MethodPatch(String name, String descriptor)
		{
			super(Opcodes.ASM4);
			this.name = name;
			this.descriptor = descriptor;
		}

		public MethodPatch apply(MethodVisitor visitor)
		{
			this.mv = visitor;
			return this;
		}

		public final void fallbackVisitMethod(int opcode, String owner, String name, String desc, boolean itf)
		{
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}
}
