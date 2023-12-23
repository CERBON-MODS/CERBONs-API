package com.cerbon.cerbons_api.api.multipart_entities.entity;

import com.cerbon.cerbons_api.api.multipart_entities.util.CompoundOrientedBox;
import net.minecraft.world.phys.AABB;

public interface MultipartEntity {
    CompoundOrientedBox getCompoundBoundingBox(AABB bounds);
}
