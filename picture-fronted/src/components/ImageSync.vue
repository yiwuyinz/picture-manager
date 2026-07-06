<template>
  <div class="ai-picture-generate">
    <a-input-group compact style="margin-bottom: 16px">
      <a-textarea
        v-model:value="prompt"
        :rows="3"
        style="width: calc(100% - 120px)"
        placeholder="请输入提示词，描述你想生成的图片"
      />
      <a-button type="primary" :loading="loading" @click="doEditPicture" style="width: 120px">
        生成
      </a-button>
    </a-input-group>

    <img
      v-if="previewUrl"
      :src="previewUrl"
      alt="generated_picture"
      style="max-width: 100%; max-height: 400px; margin-bottom: 16px; border-radius: 8px"
    />

    <ImageCropper
      ref="imageCropperRef"
      :imageUrl="previewUrl"
      :picture="props.picture"
      :spaceId="props.spaceId"
      :onSuccess="onCropSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { getImageSyncUrlUsingPost } from '@/api/pictureController'
import { message } from 'ant-design-vue'
import { ref } from 'vue'
import ImageCropper from '@/components/ImageCropper.vue'

const loading = ref<boolean>(false)
const prompt = ref<string>('')
const previewUrl = ref<string>('')

interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

const buildRequest = (promptText: string): API.ImageSyncRequest => {
  return {
    input: {
      messages: [{ content: [{ text: promptText }] }],
    },
    parameters: {
      size: '2048*2048',
    },
  }
}

/**
 * 调用 AI 生成图片
 */
const handleGenerate = async (): Promise<string | null> => {
  const promptText = prompt.value?.trim()
  if (!promptText) {
    message.warning('请输入提示词')
    return null
  }
  loading.value = true
  try {
    const res = await getImageSyncUrlUsingPost(buildRequest(promptText))
    if (res.data.code === 0 && res.data.data) {
      return res.data.data
    } else {
      message.error('图片生成失败：' + res.data.message)
      return null
    }
  } finally {
    loading.value = false
  }
}

const imageCropperRef = ref()

/**
 * 点击生成：获取图片 -> 预览 -> 打开裁剪弹窗
 */
const doEditPicture = async () => {
  const pictureUrl = await handleGenerate()
  if (!pictureUrl) {
    return
  }
  previewUrl.value = pictureUrl
  message.success('图片生成成功，请编辑')
  if (imageCropperRef.value) {
    imageCropperRef.value.openModal()
  }
}

/**
 * 裁剪成功回调
 * ⚠️ 关键：必须调用 props.onSuccess 把结果传回父组件
 */
const onCropSuccess = (newPicture: API.PictureVO) => {
  previewUrl.value = newPicture.url || ''
  // 这行是关键！通知父组件 AddPicturePage 更新 picture
  props.onSuccess?.(newPicture)
}
</script>
