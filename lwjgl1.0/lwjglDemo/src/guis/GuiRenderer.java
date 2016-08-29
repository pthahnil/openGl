package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import toolbox.Loader;

public class GuiRenderer {

	private final RawModel quard;
	private GuiShader shader;

	public GuiRenderer(Loader loader) {
		float[] positions = {-1,1,-1,-1,1,1,1,-1};
		this.quard = loader.storeDataToVAO(positions,2);
		shader = new GuiShader();
	}

	public void render(List<GuiTexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quard.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		for (GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quard.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanup();
	}
	
}
