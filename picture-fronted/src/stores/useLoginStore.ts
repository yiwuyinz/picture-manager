import { getLoginUserUsingPost } from '@/api/userController'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLoginStore = defineStore('loginUser', () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  async function fetchLoginUser() {
    //后端需要提供接口
    const res = await getLoginUserUsingPost()
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data
    }
    // setTimeout(() => {
    //     loginUser.value = {userName: '测试用户', id: 1}
    // }, 3000)
  }

  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  return { loginUser, setLoginUser, fetchLoginUser }
})
