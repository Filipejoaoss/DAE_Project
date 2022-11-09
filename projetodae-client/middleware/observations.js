export default function({$auth,redirect,route}){
    if ($auth.user.groups.includes("Patient") && $auth.user.sub != route.params.username){
      return redirect('/')
    }
    if(!$auth.user.groups.includes("Administrator") && !$auth.user.groups.includes("Medic") && !$auth.user.groups.includes("Patient")){
      return redirect('/auth/login')
    }
  }