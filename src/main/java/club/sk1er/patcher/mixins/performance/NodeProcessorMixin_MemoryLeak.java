package club.sk1er.patcher.mixins.performance;

//#if MC==10809
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NodeProcessor.class)
//#endif
public class NodeProcessorMixin_MemoryLeak {

    //#if MC==10809
    @Shadow protected IBlockAccess blockaccess;

    @Inject(method = "postProcess", at = @At("HEAD"))
    private void patcher$cleanupBlockAccess(CallbackInfo ci) {
        this.blockaccess = null;
    }
    //#endif
}

