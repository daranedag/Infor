; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
AppName=VIS2006 0.10 Versuchsflaecheninformationssystem
AppVerName=VIS2006 0.10 Versuchsflaecheninformationssystem
AppPublisher=Nordwestdeutsche Forstliche Versuchsanstalt, Goettingen
AppPublisherURL=http://www.nw-fva.de
AppSupportURL=http://www.nw-fva.de
AppUpdatesURL=http://www.nw-fva.de
DefaultDirName={pf}\VIS
DefaultGroupName=VIS
LicenseFile=H:\Programme\Sicher\Vis2006\Installation\license.txt
InfoAfterFile=H:\Programme\Sicher\Vis2006\Installation\Liesmich.txt
OutputDir=H:\Programme\Sicher\Vis2006
OutputBaseFilename=VISSetup
Compression=lzma
SolidCompression=yes

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "H:\Programme\Sicher\Vis2006\vis.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\Installation\Liesmich.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\dist\Vis2006.jar"; DestDir: "{app}\dist"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\dist\lib\*"; DestDir: "{app}\dist\lib"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\Installation\license.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\Installation\vis2006.ini"; DestDir: "{app}"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\ForestSimulatorSettings.xml"; DestDir: "{app}"; Flags: ignoreversion onlyifdoesntexist
Source: "H:\Programme\Sicher\Vis2006\ForestSimulatorSettings.xsl"; DestDir: "{app}"; Flags: ignoreversion onlyifdoesntexist
Source: "H:\Programme\Sicher\Vis2006\ArtenschlüsselST.txt"; DestDir: "{app}"; Flags: ignoreversion onlyifdoesntexist
Source: "H:\Programme\Sicher\Vis2006\Ertragstafeln\*"; DestDir: "{app}\Ertragstafeln"; Flags: ignoreversion onlyifdoesntexist
Source: "H:\Programme\Sicher\Vis2006\ns-ross.jpg"; DestDir: "{app}"; Flags: ignoreversion
Source: "H:\Programme\Sicher\Vis2006\Installation\tempdaten\visleer.mdb"; DestDir: "{app}\tempdaten"; Flags: ignoreversion onlyifdoesntexist
Source: "H:\Programme\Sicher\Vis2006\Installation\tempdaten\sicher\.mdb"; DestDir: "{app}\tempdaten\sicher"; Flags: ignoreversion onlyifdoesntexist
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\VIS"; Filename: "{app}\vis.exe" ;  WorkingDir: "{app}"
Name: "{userdesktop}\vis"; Filename: "{app}\vis.exe"; WorkingDir: "{app}"; Tasks: desktopicon

[Run]
Filename: "{app}\vis.exe"; Description: "{cm:LaunchProgram,vis}"; Flags: nowait postinstall skipifsilent

