<template>
<section class="vh-100 gradient-custom">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white">
          <div class="card-body p-5 text-center">
            <div class="mt-md-4 pb-5">
              <h3 class="fw-bold mb-3 text-uppercase">Login to MediCare</h3>
              <b-form @submit.prevent="onSubmit" @reset="onReset">
                <b-input-group class="mt-4 mb-4">
                  <b-input-group-prepend is-text>
                    <b-icon icon="person-fill"></b-icon>
                  </b-input-group-prepend>
                  <b-form-input name="username" placeholder="Username" v-model.trim="username" required></b-form-input>
                </b-input-group>
                <b-input-group class="mb-4">
                  <b-input-group-prepend is-text>
                    <b-icon icon="lock-fill"></b-icon>
                  </b-input-group-prepend>
                  <b-form-input type="password" name="password" placeholder="Password" v-model.trim="password" required></b-form-input>
                </b-input-group>
                <button class="btn btn-outline-light btn-lg px-5" type="submit">Login</button>
              </b-form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

  <!--<div>
    <b-container>
      <br>
      <h1 class="text-center">Welcome to MediCare</h1>
      <br><br>
      <b-form @submit.prevent="onSubmit" @reset="onReset">
        <b-form-group label="Username" description="Enter your username">
          <b-input
            name="username"
            placeholder="Your username"
            v-model.trim="username"
            required />
        </b-form-group>
        <b-form-group label="Password" description="Enter your password">
          <b-input
            name="password"
            type="password"
            placeholder="Your password"
            v-model="password"
            required />
        </b-form-group>
        <b-button type="submit" class="">Login</b-button>
      </b-form>
    </b-container>
  </div>-->
</template>

<script>
export default {
 auth: false,
 data() {
    return {
        username: null,
        password: null
    }
 },
 methods: {
 onSubmit() {
    let promise = this.$auth.loginWith('local', {
        data: {
            username: this.username,
            password: this.password
        }
    })
    promise.then(() => {
        this.$toast.success('You are logged in!').goAway(3000)
        // check if the user $auth.user object is set
        // TODO redirect based on the user role

        // eg:
        if (this.$auth.user.groups.includes('Administrator')) {
            this.$router.push('/')
        } else if (this.$auth.user.groups.includes('Medic')) {
            this.$router.push('/')
        } else if (this.$auth.user.groups.includes('Patient')) {
            this.$router.push('/')
        }

    })
    promise.catch(() => {
        this.$toast.error('Sorry, you cant login. Ensure your credentials are correct').goAway(3000)
    })
 },
 onReset() {
    this.username = null
    this.password = null
  }
 }
}
</script>

<style scoped>
  .gradient-custom {
    /* fallback for old browsers */
    /* Chrome 10-25, Safari 5.1-6 */
    background: -webkit-linear-gradient(to right, #13b9a3, rgb(0, 255, 191));
    /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    background-color: #08AEEA;
    background-image: linear-gradient(0deg, #08AEEA 0%, #2AF598 100%);

  }
  .card {
    border-radius: 1rem;
    box-shadow: rgb(38, 57, 77) 0px 20px 30px -10px; 
  }
</style>