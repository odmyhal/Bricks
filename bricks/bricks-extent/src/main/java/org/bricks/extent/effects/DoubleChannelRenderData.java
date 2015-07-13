package org.bricks.extent.effects;

import java.util.Map.Entry;
import org.bricks.utils.LoopMap;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.ChannelDescriptor;

public class DoubleChannelRenderData {
	
	private static final ChannelDescriptor[] descriptors = new ChannelDescriptor[]{ParticleChannels.Position, ParticleChannels.Scale, ParticleChannels.Rotation2D, ParticleChannels.Rotation3D, ParticleChannels.Color};
	
	private ParticleController controller;
	private LoopMap<ChannelDescriptor, FloatChannel>[] channels = new LoopMap[2];
	private SubChannelRenderer sbRenderer;
	
	protected DoubleChannelRenderData(ParticleController controller, SubChannelRenderer renderer){
		this.controller = controller;
		this.sbRenderer = renderer;
		for(int i = 0; i < channels.length; i++){
			LoopMap<ChannelDescriptor, FloatChannel> channelBox = new LoopMap<ChannelDescriptor, FloatChannel>();
			for(int j = 0; j < descriptors.length; j++){
				FloatChannel channel = controller.particles.getChannel(descriptors[j]);
				if(channel != null){
					channelBox.put(descriptors[j], controller.particles.new FloatChannel(channel.id, channel.strideSize, channel.data.length / channel.strideSize));
				}
			}
			channels[i] = channelBox;
		}
	}
	
	protected void flushRenderData(int index){
		for(Entry<ChannelDescriptor, FloatChannel> entry : channels[index].entryLoop()){
			FloatChannel srcChannel = controller.particles.getChannel(entry.getKey());
			FloatChannel dstChannel = entry.getValue();
			System.arraycopy(srcChannel.data, 0, dstChannel.data, 0, controller.particles.size * srcChannel.strideSize);
		}
	}
	
	protected void setChannelDataSize(int size){
		sbRenderer.setControllerSize(size);
	}
	
	protected void substituteRendererData(int index){
		for(Entry<ChannelDescriptor, FloatChannel> entry : channels[index].entryLoop()){
			if( entry.getKey().id == ParticleChannels.Position.id ){
				sbRenderer.setPositionChannel(entry.getValue());
			}else if(entry.getKey().id == ParticleChannels.Scale.id){
				sbRenderer.setScaleChannel(entry.getValue());
			}else if(entry.getKey().id == ParticleChannels.Rotation2D.id){
				sbRenderer.setRotation2DChannel(entry.getValue());
			}else if(entry.getKey().id == ParticleChannels.Rotation3D.id){
				sbRenderer.setRotation3DChannel(entry.getValue());
			}else if(entry.getKey().id == ParticleChannels.Color.id){
				sbRenderer.setColorChannel(entry.getValue());
			}
		}
	}
}
