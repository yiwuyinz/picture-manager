<template>
  <div id="userLoginPage">
    <h2 class="title">云图库应用-用户信息修改</h2>
    <div class="desc">企业级智能协同云图库</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item
        label="用户账号"
        name="userAccount"
        :rules="[{ required: true, message: '请输入账号' }]"
      >
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        label="用户名"
        name="userName"
        :rules="[{ required: true, message: '请输入用户名' }]"
      >
        <a-input v-model:value="formState.userName" />
      </a-form-item>
      <a-form-item label="用户头像" name="userAvatar">
        <a-input v-model:value="formState.userAvatar" />
      </a-form-item>
      <a-form-item label="用户简介" name="userProfile">
        <a-input v-model:value="formState.userProfile" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">修改</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { updateUserUsingPost, userLoginUsingPost } from '@/api/userController'
import { useLoginStore } from '@/stores/useLoginStore'
import { message } from 'ant-design-vue'
import { onMounted, reactive } from 'vue'
import router from '@/router'

interface FormState {
  userAccount: string
  userName: string
}

const formState = reactive<API.UserUpdateRequest>({
  id: '',
  userAccount: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

const loginUserStore = useLoginStore()

const fetchUserData = async () => {
  const loginUser = loginUserStore.loginUser
  formState.id = loginUser.id
  formState.userAccount = loginUser.userAccount
  formState.userName = loginUser.userName
  formState.userAvatar = loginUser.userAvatar
  formState.userProfile = loginUser.userProfile
  formState.userRole = loginUser.userRole
}

const handleSubmit = async (values: any) => {
  if (!values) {
    return
  }
  const submitData = {
    ...values,
    id: formState.id,
    userRole: formState.userRole,
  }
  const res = await updateUserUsingPost(submitData)
  if (res.data.code === 0) {
    message.success('更新成功')
    await loginUserStore.fetchLoginUser()
  } else {
    message.error('更新失败' + res.data.message)
  }
}

onMounted(() => {
  fetchUserData()
})
</script>
<style>
#userLoginPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 32px;
}
</style>
