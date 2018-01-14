import os
import subprocess

directory = "data/bus_routing/"
config    = "config/config.properties"
for filename in os.listdir(directory):
    fn = os.path.join(directory, filename)
    print("RUNNING %s" % fn)
    subprocess.Popen("java -jar sbrp_ga.jar %s %s" % (config, fn)).wait()
