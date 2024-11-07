# Bucket Service

This service uses the S3 Reactive Web dependency to handle file uploads to an Amazon S3 bucket. Ensure that the AWS profile is correctly configured for the service to function.

## Usage of S3 Reactive Web

This service utilizes the S3 Reactive Web dependency to manage file uploads to an Amazon S3 bucket. Make sure to have the AWS profile configured correctly for the service to work.

## Configuration of Profile Name on Windows and Linux

### Windows

1. Open `PowerShell` or `Command Prompt`.
2. Run the following command to set the profile name:

    ```powershell
    Set-Item -Path Env:AWS_PROFILE -Value "s3-config"
    ```

3. Verify that the environment variable is set correctly:

    ```powershell
    Get-Item -Path Env:AWS_PROFILE
    ```

### Linux

1. Open a terminal.
2. Run the following command to set the profile name:

    ```bash
    export AWS_PROFILE=s3-config
    ```

3. Verify that the environment variable is set correctly:

    ```bash
    echo $AWS_PROFILE
    ```

## Installation of AWS CLI

To configure the profile name, you need to have AWS CLI installed. Below are the steps to install it on Windows and Linux.

### Windows

1. Download the AWS CLI installer from [this link](https://aws.amazon.com/cli/).
2. Run the installer and follow the on-screen instructions.

### Linux

1. Open a terminal.
2. Run the following commands to install AWS CLI:

    ```bash
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip awscliv2.zip
    sudo ./aws/install
    ```

3. Verify the installation:

    ```bash
    aws --version
    ```

## Configuration of AWS Profile

1. Run the following command to configure the AWS profile:

    ```bash
    aws configure --profile s3-config
    ```

2. Enter your AWS credentials when prompted.

With these steps, you will have correctly configured the profile name so that the service can use the S3 Reactive Web dependency.