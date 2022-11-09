<template>
  <div class="h-screen bg-gray-100">
    <div class="flex flex-row-reverse">
      <div class="m-2">
        <button
          class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
          v-on:click="logout"
        >
          Logout
        </button>
      </div>
    </div>
    <div class="flex justify-center">
      <div class="w-256">
        <div class="flex items-center p-2 border rounded">
          <label class="p-2"> Patient </label>
          <input
            class="border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            v-model="patient"
          />
        </div>
        <br />
        <div class="flex items-center p-2 border rounded">
          <label class="p-2"> Data Type </label>
          <select
            class="border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            v-model="dataType"
          >
            <option v-for="data in dataTypes" :key="data.id" :value="data.id">{{data.name}}</option>
          </select>
        </div>
        <br />
        <div class="flex items-center p-2 border rounded">
          <label class="p-2"> Qualitative </label>
          <input
            class="border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            v-model="qualitative"
          />
        </div>
        <br />
        <div class="flex w-full">
          <button
            class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            v-on:click="startStop"
          >
            {{ msg }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "IndexPage",
  data() {
    return {
      dataTypes: [],
      timer: undefined,
      msg: "Start Sensor",
      patient: "",
      dataType: null,
      qualitative: "",

    };
  },
  methods: {
    now(){
      let now = new Date(Date.now())
      return {
        hour: now.getHours(),
        minute: now.getMinutes(),
        second: now.getSeconds(),
        day: now.getDate(),
        month: now.getMonth()+1,
        year: now.getFullYear(),
      }
    },
    startStop() {
      console.log("clicked")
      if (this.timer == undefined) {
        this.msg = "Stop Sensor";
        this.timer = setInterval(this.sendData, 5000);
      } else {
        this.msg = "Start Sensor";
        clearInterval(this.timer)
      }
    },
    sendData() {
      this.$axios
        .$post(`/api/observations/${this.patient}`, {
          patientUsername: this.patient,
          examiner: navigator.userAgent,
          dateTime: this.now(),
          biomedicDataType: {id: this.dataType},
          qualitative: this.qualitative,
          value: (Math.random()*150) + 50
        })
        .then((response) => {
          console.log("Registred: ")
          console.log(response.data)
        })
        .catch((error) => {
          console.log(error.response.data);
        });
    },
    logout() {
      this.$auth.logout();
      this.$router.push("/");
    },
  },
  created() {
    this.$axios.get(`/api/biomedicDataTypes/`).then((response) => {
      this.dataTypes = response.data
    })
  }
};
</script>
