package net.wigoftime.open_komodo.custommobs;

import net.minecraft.server.v1_16_R1.ControllerMove;
import net.minecraft.server.v1_16_R1.EntityInsentient;

public class CustomMoveController extends ControllerMove {
    public CustomMoveController(EntityInsentient var0) {
        super(var0);
    }

    public void a(float var0, float var1) {
        this.h = ControllerMove.Operation.STRAFE;
        this.f = var0;
        this.g = var1;
        this.e = 1;
    }
}
