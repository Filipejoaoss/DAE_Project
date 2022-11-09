
export default function({$auth,redirect,route}){
  if($auth.user.groups.includes("Administrator") && $auth.user.sub != route.params.username) {
    return redirect('/administrators')
  }

  if(!$auth.user.groups.includes("Administrator")){
      if($auth.user.groups.includes("Patient")){
          return redirect('/patients')
      } else if ($auth.user.groups.includes("Medic")){
          return redirect('/medics')
      }
  }
}

