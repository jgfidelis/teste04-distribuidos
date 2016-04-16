import os
import math
print "Antes de rodar o script de um chmod +x sinalgo"
node =  [512, 1024, 2048, 4096,8192]
r = 10000
density = [10,15,20,25,30]
for n in node:
    for d in density:
        x = round(math.sqrt(math.pi*n*r/d))    
        #print str() + ' ' + str(n)+ ' '+ str(d) ;        
        os.system("./sinalgo -project PIF -gen "+str(n)+" PIF:PIFNode Random -rounds 1000000 -batch -overwrite useFixedSeed=false dimX="+str(int(x))+" dimY="+str(int(x))+" > out_"+str(n)+"_"+str(d)+".txt")
