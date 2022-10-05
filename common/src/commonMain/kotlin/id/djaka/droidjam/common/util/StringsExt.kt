package id.djaka.droidjam.common.util

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceStringDesc
import dev.icerock.moko.resources.desc.StringDesc

fun stringResource(res: StringResource): ResourceStringDesc {
    return StringDesc.Resource(res)
}

