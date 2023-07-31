#!/bin/sh

# Check if the package is found (0 is true)
# Installing dependencies
packages="openjdk-11-jdk espeak"
isXdotool=0



# Check if X11 --> add xdotool (optional; yes or no)
if [ "$WAYLAND_DISPLAY" != "wayland-0" ]; then
    # X11
    xdotoolAdd=$(dpkg-query -W --showformat='${Status}\n' "xdotool" | grep "install ok installed")
    if [ -z "$xdotoolAdd" ]; then
        printf "Do you wish to install xdotool? [y/N]: "
        read -r answer
        answer="${answer:-N}"
        case "$answer" in
            [Yy]*) sudo apt-get install xdotool ;;
        esac
    fi
    isXdotool=0 # setting isXdotool is true
else
    # Wayland
    echo "Display: Wayland (xdotool does not support Wayland)"
    isXdotool=1 # setting isXdotool is false
fi

foundPkgs=0
# Installing packages
for pkg in $packages; do
    foundPkgs=$((foundPkgs + 1))
    is_pkg_installed=$(dpkg -s "$pkg" | grep "install ok installed")

    if [ -n "$is_pkg_installed" ]; then
        echo "$pkg is installed."
    else
        echo "$pkg is required to proceed"
        echo "Installing $pkg in 5 seconds..."
        sleep 5
        sudo apt -y install "$pkg"
    fi
done

# Compile and run
if [ "$foundPkgs" -eq $(echo "$packages" | wc -w) ]; then
    ./gradlew clean createFatJar

    # Zooms in four times then zoom out four times
    echo "Is xdotool on? $isXdotool"
    if [ "$isXdotool" -eq 0 ]; then
        for i in $(seq 1 4); do xdotool key Ctrl+plus; done
    fi

    if [ "$1" = "-h" ]; then
     echo "HELP:"
     echo "./run [num of questions] [max range from 0]"
     echo "Negative numbers are not permitted."
     exit 0
     
     fi
     if [ "$#" -eq 0 ]; then
        ./gradlew -q --console plain run
     elif [ "$#" -eq 2 ]; then
        ./gradlew -q --console plain run --args="$1 $2"
     elif [ "$#" -eq 1 ]; then
        ./gradlew -q --console plain run --args="$1"
     else
        echo "Invalid number of arguments"
        exit 1
     fi

    # Zoom out when done
    if [ "$isXdotool" -eq 0 ]; then
        for i in $(seq 1 4); do xdotool key Ctrl+minus; done
    fi
fi
