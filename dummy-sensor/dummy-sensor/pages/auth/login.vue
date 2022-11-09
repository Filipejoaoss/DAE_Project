<template>
  <div class="justify-center flex h-screen">
    <div class="justify-center flex">
      <div class="w-full">
        <div class="mb-4">
          <label
            class="block text-gray-700 text-sm font-bold mb-2"
            for="username"
          >
            Username
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="username"
            type="text"
            placeholder="Username"
            v-model.trim="username"
          />
        </div>
        <div class="mb-6">
          <label
            class="block text-gray-700 text-sm font-bold mb-2"
            for="password"
          >
            Password
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
            id="password"
            type="password"
            placeholder="****"
            v-model="password"
          />
          <p class="text-xs italic">Please choose a password.</p>
        </div>
        <div class="flex items-center justify-between">
          <button
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            type="button"
            v-on:click="onSubmit"
          >
            Sign In
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  auth: false,
  data() {
    return {
      username: null,
      password: null,
    };
  },
  methods: {
    onSubmit() {
      let promise = this.$auth.loginWith("local", {
        data: {
          username: this.username,
          password: this.password,
        },
      });
      promise.then(() => {
        this.$toast.success("You are logged in!").goAway(3000);
        // check if the user $auth.user object is set
        // TODO redirect based on the user role

        // eg:
        if (this.$auth.user.groups.includes("Administrator")) {
          this.$router.push("/");
        } else if (this.$auth.user.groups.includes("Medic")) {
          this.$router.push("/");
        } else if (this.$auth.user.groups.includes("Patient")) {
          this.$router.push("/");
        }
      });
      promise.catch(() => {
        this.$toast
          .error("Sorry, you cant login. Ensure your credentials are correct")
          .goAway(3000);
      });
    },
    onReset() {
      this.username = null;
      this.password = null;
    },
  },
};
</script>
