#!/usr/bin/ruby

# Tool to download RPG sources from AS400 via FTP
# If you don't specify any command line argument, the program will ask for server name, user, etc.
# If you pass the name of a configuration, it searches it in the ftpas.yml file in your user home,
# getting the connection parameters from there or, of they are not present, asking them from console
# You can find a sample config file in ./ftpas.sample.yaml
# Remember you can use * in member specification
# Example call:
# ruby ftpas.rb lab

require 'net/ftp'
require 'yaml'
require 'io/console'

def notSpecified?(x)
    x.nil? || x.strip.empty?
end

def fromConfigOrConsole(conf, key, hidden=false)
    x = conf.nil? ? nil : conf[key]
    if notSpecified?(x)
        puts "Enter " + key
        if hidden == true
            x = STDIN.noecho(&:gets).chomp
        else
            x = gets.chomp
        end
    end
    return x
end

configFileName = "#{Dir.home}/ftpas.yml"

if File.exists? (configFileName)
    configs = YAML.load_file(configFileName)
else
    configs = {}
end

currentConfig = configs[ARGV[0]]

server = fromConfigOrConsole(currentConfig, "server")
user = fromConfigOrConsole(currentConfig, "user")
pwd = fromConfigOrConsole(currentConfig, "password", true)
library = fromConfigOrConsole(currentConfig, "library")
file = fromConfigOrConsole(currentConfig, "file")
member = fromConfigOrConsole(currentConfig, "member")

ftp = Net::FTP::new(server)
ftp.debug_mode = true
puts "Connecting to server..."
ftp.login(user, pwd)

puts "Connected!"
ftp.chdir(library)
fileList = ftp.nlst(file + "." + member)
fileList.each do |file|
    name = file.split('.')
    localName = name.last
    ftp.gettextfile(file, localName)
    (system("iconv -f WINDOWS-1252 -t UTF-8 #{localName} >> #{localName}.rpgle") and File.delete(localName)) or
    puts "iconv failed for file #{localName}: #{$?}"
end
ftp.close
puts "OK"