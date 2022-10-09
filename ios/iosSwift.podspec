Pod::Spec.new do |spec|
    spec.name                     = 'iosSwift'
    spec.version                  = '1.0'
    spec.homepage                 = 'Link to a Kotlin/Native module homepage'
    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Some description for a Kotlin/Native module'
    spec.module_name              = "SharedSwift"
    
    
    spec.static_framework         = false
    spec.dependency 'ios'
    spec.source_files = "build/cocoapods/framework/SharedSwift/**/*.{h,m,swift}"
end