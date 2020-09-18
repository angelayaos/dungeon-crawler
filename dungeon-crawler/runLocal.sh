#!/bin/bash
# By
# ████████╗██╗   ██╗ █████╗ ███╗   ██╗    ██╗  ██╗ ██████╗ 
# ╚══██╔══╝██║   ██║██╔══██╗████╗  ██║    ██║  ██║██╔═══██╗
#    ██║   ██║   ██║███████║██╔██╗ ██║    ███████║██║   ██║
#    ██║   ██║   ██║██╔══██║██║╚██╗██║    ██╔══██║██║   ██║
#    ██║   ╚██████╔╝██║  ██║██║ ╚████║    ██║  ██║╚██████╔╝
#    ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝    ╚═╝  ╚═╝ ╚═════╝ 

# Email: ttuan.ho@outlook.com                                                         



# #######################################
# Scripts to run on Tuan's local Mac    #
# #######################################
# source /Users/tuanho/.bash_profile
# 2511projectjavadocs 
# echo "Genereated javadocs"

# # Finalizing docs-related
# cd docs/
# if test -f /Volumes/Macintosh\ HD/Downloads/2511project.png 
# then
#     mv /Volumes/Macintosh\ HD/Downloads/2511project.png .
#     chmod 644 ./2511project.png 
#     pandoc ./design.md -o ../design.pdf
# fi
# cd ../
# echo "Finalized docs"

# #######################################
# Checking the submission size          #
# #######################################

git bundle create submission.bundle master || exit 1 && du -sh submission.bundle && rm submission.bundle
echo " >> Is the size of the bundle over 30 MB?"

# #######################################
# Running java programs                 #
# #######################################
rm -fr ./bin
rm -fr ./src/*bin
javac -cp ".:./src:lib/*" -d "./bin" src/unsw/dungeon/*.java
echo "Compiled code"
echo "Running code"
cp src/unsw/dungeon/*.fxml ./bin/unsw/dungeon
java -cp "./bin:.:lib/*" --module-path "lib/:bin:src/unsw/dungeon" --add-modules=javafx.controls,javafx.fxml  unsw.dungeon.DungeonApplication
