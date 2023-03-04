#!/usr/bin/env python
#

# Import Packages
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize      import fsolve
import scipy.integrate as sp
from scipy.interpolate   import interp1d





# Physical Properties
g = 9.81 # m/s^2, acceleration due to gravity



# Interpolation of air density by height
rho = np.array([1.347,1.225, 1.112, 1.007, 0.9093, 0.8194])
h = np.array([-1000,0, 1000, 2000, 3000, 4000])
density = interp1d(h, rho)



### YOU CAN CHANGE THESE ######
Cd_r = .45 # Drag coefficient of rocket
Ar = .0188866 # Area of rocket, m^2
Cd_b = 1.28 # Drag coefficient of brakes
w = .0762 # m
Li = .0762/2 #m
r = .0762/3 #m
m = 14.841542 # Weight of rocket, kg
the0 = 0 # radians, 0 means min is furthest from full extension

target = 3048 # Target final elevation, m
cut_v = 348.5 # velocity after boosters, m/s
cut_y = 818.4 # height after boosters, m

#theta = np.cosh(((x+Li-r)**2 - Li**2 - r**2)/(-2*Li*r))

def rates(init,time):
    
    # set values with current iteration
    v = init[0]
    y = init[1]
    theta = init[2]
    
    # Find air density at current height
    dens = density(y)
    

    x = np.sqrt(Li**2+r**2-2*Li*r*np.cos(theta)) + r - Li
    
    Ab = w*x
    
    
    # differentials
    dv = -(Cd_r*Ar+Cd_b*Ab)*dens*v**2/2/m - g
    
    dy = v
    dthe = 0
    
    return (np.array([dv,dy,dthe]))

# create an array of thetas (brake angle) to compare
the = np.linspace(0+the0,np.pi,100)  ##### DO NOT EXCEEED PI OR INTERP WILL BREAK

max_heights = np.array([0])

# find maximum height for every theta (brake angle)
for x in range(len(the)):
    init = np.array([cut_v,cut_y,the[x]])


    time = np.linspace(0,30,1000)

    values = sp.odeint(rates,init,time)
    velocity = values[:,0]
    height = values[:,1]
    

    for i in range(len(height)-1):
        if(height[i]>height[i+1]):
            print('Maximum height is', height[i],'m')
            if (len(max_heights) == 1 and max_heights[0] == 0):
                max_heights[0] = height[i]
            else:
                max_heights = np.append(max_heights,height[i])
            break

# Visualize the results
plt.plot(the,max_heights,label="height")
plt.legend()
plt.xlabel('theta')
plt.ylabel('max height (m)')
plt.show()

ideal_the = interp1d(max_heights,the)


print('target is reached at theta = ',ideal_the(target), 'radians')
print(ideal_the(target)-the0,'radians from start position')


