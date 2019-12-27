-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: Once you have a command window up in the folder that the downloaded material was placed, type:

Windows: "gradlew setupDecompWorkspace"
Linux/Mac OS: "./gradlew setupDecompWorkspace"

Step 3: After all that finished, you're left with a choice.
For eclipse, run "gradlew eclipse" (./gradlew eclipse if you are on Mac/Linux)

If you prefer to use IntelliJ, steps are a little different.
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Once it's finished you must close IntelliJ and run the following command:

"gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)

Step 4: The final step is to open Eclipse and switch your workspace to /eclipse/ (if you use IDEA, it should automatically start on your project)

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.

Tip:
If you do not care about seeing Minecraft's source code you can replace "setupDecompWorkspace" with one of the following:
"setupDevWorkspace": Will patch, deobfuscate, and gather required assets to run minecraft, but will not generate human readable source code.
"setupCIWorkspace": Same as Dev but will not download any assets. This is useful in build servers as it is the fastest because it does the least work.

Tip:
When using Decomp workspace, the Minecraft source code is NOT added to your workspace in a editable way. Minecraft is treated like a normal Library. Sources are there for documentation and research purposes and usually can be accessed under the 'referenced libraries' section of your IDE.

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html

=============================================================================================

-------------------------------------------
modders的源安装信息
-------------------------------------------
此代码遵循Minecraft Forge安装方法。它将适用
一些小补丁到香草MCP源代码，让你和它访问
对于一些数据和函数，你需要建立一个成功的模型。
还要注意的是，这些补丁是根据“未命名”的MCP源代码（aka
srgnames）-这意味着您将无法直接阅读它们
正常代码。
源包安装信息：
独立源安装
==============================
有关更多详细说明，请参阅Forge联机文档：
http://mcforge.readthedocs.io/en/latest/gettingstarted/

步骤1：打开命令行并浏览到解压缩zip文件的文件夹。

第2步：在下载的材料所在的文件夹中打开命令窗口后，键入：
Windows：“gradlew setupDecompWorkspace”
Linux/Mac操作系统：“./gradlew setupDecompWorkspace”

第三步：做完这些，你还有选择的余地。
对于eclipse，运行“gradlew eclipse”（/gradlew eclipse如果您在Mac/Linux上）
如果您更喜欢使用IntelliJ，则步骤有点不同。
一。开放思想，导入项目。
2。选择build.gradle文件并将其导入。
三。完成后，必须关闭IntelliJ并运行以下命令：
“gradlew genIntellijRuns”（/gradlew genIntellijRuns，如果您在Mac/Linux上）

第4步：最后一步是打开Eclipse并将您的工作区切换到/Eclipse/（如果您使用IDEA，它应该自动启动您的项目）
如果在任何时候IDE中缺少库，或者遇到问题，可以运行“gradlew --refresh-dependencies”来刷新本地缓存gradlew clean“重置所有内容{这不会影响代码}，然后再次启动进程。
如果还是不行，
有关gradle环境的更多信息，请参阅EsperNet上的ForgeGradle。
提示：
如果您不想看到Minecraft的源代码，可以将“setupDecompWorkspace”替换为以下之一：
“setupDevWorkspace”：将修补、清除和收集运行minecraft所需的资源，但不会生成人类可读的源代码。
“setupCIWorkspace”：与Dev相同，但不会下载任何资源。这在构建服务器时很有用，因为它是最快的，因为它所做的工作最少。

提示：
使用Decomp workspace时，Minecraft源代码不会以可编辑的方式添加到您的工作区中。Minecraft被视为一个普通的图书馆。这些资源用于文档和研究目的，通常可以在IDE的“引用库”部分中访问。
锻造源安装
=========================
MinecraftForge使用此代码进行装运，并将其作为锻造的一部分进行安装
安装过程中，您不需要采取进一步的措施。
莱克斯马诺斯的安装视频
=======================
https://www.youtube.com/watch？v=8VEdtQLuLO0和feature=youtu.be
有关更多详细信息，请参阅Forge论坛：
http://www.minecraftforge.net/forum/index.php/topic，14048.0.html