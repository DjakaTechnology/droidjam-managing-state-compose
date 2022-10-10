//
//  SuspendExtension.swift
//  droidjam.ios
//
//  Created by Djaka Pradana Jaya on 08/10/22.
//

import Foundation
import RxSwift
import Shared


func createObservable<T>(
    scope: CoroutineScope,
    flowWrapper: FlowWrapper<T>
) -> Observable<T> {
    return Observable<T>.create { observer in
        let job: Job = flowWrapper.subscribe(
            scope: scope,
            onEach: { item in observer.on(.next(item!)) },
            onComplete: { observer.on(.completed) },
            onThrow: { error in observer.on(.error(KotlinError(error))) }
        )
        return Disposables.create { job.cancel(cause: nil) }
    }
}

func createSingle<T>(
    scope: CoroutineScope,
    suspendWrapper: SuspendedWrapper<T>
) -> Single<T> {
    return Single<T>.create { single in
        let job: Job = suspendWrapper.subscribe(
            scope: scope,
            onSuccess: { item in single(.success(item!)) },
            onThrow: { error in
                // TODO: Figure out why it doesn't compile
//                error in single(.error(KotlinError(error)))
                
            }
        )
        return Disposables.create { job.cancel(cause: nil) }
    }
}

class KotlinError: LocalizedError {
    let throwable: KotlinThrowable
    init(_ throwable: KotlinThrowable) {
        self.throwable = throwable
    }
    var errorDescription: String? {
        get { throwable.message }
    }
}
