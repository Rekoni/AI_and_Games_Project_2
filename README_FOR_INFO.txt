A. Linear Regression Approach

A mathematical model used to predict how a particular set of data would
behave it the relation between variables is assumed linear.

In order to be able to calculate the predictor variable, the variables
"a" and "b" that describe a line y (prediction) = a * x + b two approaches were used:

I. Standard Linear Regression:

This model was inspired from the lecture slides and can be found under
/model/StandarLinearRegression.java. It calculates a and b based on the 
values obtained from the interface. In order to achieve this the following are
required:

  - sumOfFollower = sum_of_all(followerPrice)
  - squaredSumOfLeader = sum_of_all(leaderPrice_squared)
  - sumOfLeader = sum_of_all(leaderPrice)
  - crossSum = sum_of_all(followerPrice x leaderPrice)

The sum is over the number of days the model takes into consideration

This approach did not yield consistent and satisfying results.

II. Least Square Approach

This approach was inspired from a Princeton University Java package and the
code we wrote can be found under /models/LinearRegressionPrinceton.java

It is different in the sense that the slope is computed by first normalizing
the sum by substracting the mean of all the values of the follower and
the leader and then computing the values a and b.

In this case "a" will be equal to the divison of the sum of products of the 
normalized values of the follower and leader. Doing this will also enable for
"b" to be calculated as we know the averages so it becomes:
b = average_of_the_follower - a * average_of_the_leader

This model offers significant improvements.


Forgetting Factor: 

In /models/LinearRegressionPrincetonForget.java a forgetting factor is also 
included. The weights is then calculcated for each value. More recent values
have a bigger weight as they are substantial.

This model did not seem to produce significant improvements


B. Polynomial Model:

