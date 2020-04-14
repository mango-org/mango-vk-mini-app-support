<?php

namespace php\vk\miniapp;

class HMACSignVerifier
{
    /**
     * @param string $query
     * @param string $clientSecret
     * @return bool
     */
    public static function verify(string $query, string $clientSecret): bool {}

    /**
     * @param string $query
     * @return array
     */
    public static function getQueryParams(string $query): array {}
}
