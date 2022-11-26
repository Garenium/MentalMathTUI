
#check if the package is found (0 is true)
#installing dependencies
packages=("openjdk-11-jdk" "espeak")
isXdotool=0

#check if X11 --> add xdotool (optional; yes or no)
if [ "$WAYLAND_DISPLAY" != "wayland-0" ]; then
    #X11
    xdotoolAdd=$(dpkg-query -W --showformat='${Status}\n' "xdotool" | grep "install ok installed")
    if [  "$xdotoolAdd" == "" ]; then
        read -p "Do you wish to install xdotool? [y/N]: " answer
        answer="${answer:-N}"
        [[ $answer =~ [Yy] ]] && sudo apt-get install xdotool
    fi
    isXdotool=$((notWayland+1)) #setting isXdotool is false
else
    #Wayland
    echo "Display: Wayland (xdotool does not support Wayland)"
fi

foundPkgs=0
#installing packages
for pkg in ${packages[@]}; do

    foundPkgs=$((foundPkgs+1))
    is_pkg_installed=$(dpkg -s ${pkg} | grep "install ok installed")

    if [ "${is_pkg_installed}" == "Status: install ok installed" ];
    then
        echo ${pkg} is installed.
    else
        echo "${pkg} is required to proceed"
        echo "Installing ${pkg} in 5 seconds..."
        sleep 5
        sudo apt -y install ${pkg}
    fi
done

#compile and run
if [ ${#packages[@]} -eq ${foundPkgs} ]
then

    #zooms in four times then zoom out four times
     [ $isXdotool -eq 0 ] && for i in {1..4}; do xdotool key Ctrl+plus; done

     ./gradlew clean createFatJar

     if [ $# -eq 0 ]; then
        ./gradlew -q --console plain run 
     elif [ $# -eq 2 ]; then
        ./gradlew -q --console plain run --args="$1 $2"
     elif [ $# -eq 1 ]; then
        ./gradlew -q --console plain run --args="$1"
    else 
        echo "Invalid number of arguments"
        exit
     fi

    [ $isXdotool -eq 0 ] && for i in {1..4}; do xdotool key Ctrl+minus; done
fi



