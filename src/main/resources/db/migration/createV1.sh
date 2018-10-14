#!/bin/bash
pg_dump -h localhost -U adempiere idempiere -p 5433 >V1__iDempiere_5.1.0.sql